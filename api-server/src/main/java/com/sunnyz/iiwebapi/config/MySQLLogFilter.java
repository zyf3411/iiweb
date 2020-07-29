package com.sunnyz.iiwebapi.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySQLLogFilter extends TurboFilter {
    private String showCaller = "true";
    private String hideFieldsInSelect = "true";
    private String callerPackageInclude;
    private String callerPackageExclude;
    private String[] callerPackageIncludes;
    private String[] callerPackageExcludes;
    private static Pattern regSqlExtractor = Pattern.compile("^(QUERY|EXECUTE|PREPARE) created\\:.+duration: ([\\d]+) connection-id: ([\\d]+).+ message\\: (.*)", 32);

    public MySQLLogFilter() {
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        if (level == Level.INFO && logger.getName().equals("MySQL")) {
            if (format.startsWith("QUERY created") || format.startsWith("EXECUTE created") || format.startsWith("PREPARE created")) {
                Matcher m = regSqlExtractor.matcher(format);
                if (m.find() && m.group().length() > 4) {
                    String duration = m.group(2);
                    String connId = m.group(3);
                    String sql = m.group(4);
                    if (sql.startsWith("/*") || sql.toUpperCase().startsWith("SET")) {
                        return FilterReply.DENY;
                    }

                    if (sql.startsWith("SHOW WARNINGS")) {
                        return FilterReply.DENY;
                    }

                    if (sql.contains("@@session.tx_read_only")) {
                        return FilterReply.DENY;
                    }

                    if (this.hideFieldsInSelect != null && this.hideFieldsInSelect.equals("true") && !sql.contains("^_^")) {
                        sql = this.doHideFieldsInSelect(sql);
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("mysql> ");
                    if (format.startsWith("PREPARE created")) {
                        sb.append("PREPARE: ");
                    }

                    sb.append(sql).append(" (duration:").append(duration).append(") c0nn").append(connId);
                    if (this.showCaller != null && this.showCaller.equals("true")) {
                        sb.append(" ").append(this.getCaller());
                    }

                    logger.info(marker, sb.toString(), params);
                }

                return FilterReply.DENY;
            }

            if (format.startsWith("[FETCH]")) {
                return FilterReply.DENY;
            }
        }

        return FilterReply.NEUTRAL;
    }

    private String doHideFieldsInSelect(String sql) {
        if (sql != null) {
            String sqlInUppercase = sql.toUpperCase();
            if (sqlInUppercase.startsWith("SELECT") && !sqlInUppercase.startsWith("SELECT COUNT")) {
                int fromPos = sqlInUppercase.indexOf(" FROM ");
                if (fromPos > 0) {
                    String fields = sqlInUppercase.substring(0, fromPos);
                    if (fields.indexOf("COUNT(") >= 0 || fields.indexOf("SUM(") >= 0 || fields.indexOf("AVG(") >= 0) {
                        return sql;
                    }

                    sql = "SELECT  *  " + sql.substring(fromPos + 1);
                }
            }
        }

        return sql;
    }

    private String getCaller() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        String previousClass = null;
        new ArrayList();

        for(int i = 5; i < stElements.length; ++i) {
            StackTraceElement ste = stElements[i];
            String clazz = ste.getClassName();
            boolean shouldInclude = true;
            if (this.callerPackageInclude != null) {
                shouldInclude = false;

                for(int j = 0; j < this.callerPackageIncludes.length; ++j) {
                    if (clazz.startsWith(this.callerPackageIncludes[j].trim())) {
                        shouldInclude = true;
                        break;
                    }
                }
            }

            boolean shouldExclude = false;
            if (this.callerPackageExclude != null) {
                for(int j = 0; j < this.callerPackageExcludes.length; ++j) {
                    if (clazz.startsWith(this.callerPackageExcludes[j].trim())) {
                        shouldExclude = true;
                        break;
                    }
                }
            }

            if (shouldInclude && !shouldExclude) {
                if (previousClass == null) {
                    previousClass = clazz;
                }

                if (!previousClass.equals(clazz)) {
                    sb.append("@").append(previousClass.substring(previousClass.lastIndexOf(".") + 1)).append(" ");
                    previousClass = clazz;
                }

                if (sb.length() > 0) {
                    sb.append("<");
                }

                sb.append(ste.getMethodName());
            }
        }

        if (previousClass != null) {
            sb.append("@").append(previousClass.substring(previousClass.lastIndexOf(".") + 1));
        }

        return sb.toString();
    }

    public void setHideFieldsInSelect(String hideFieldsInSelect) {
        this.hideFieldsInSelect = hideFieldsInSelect;
    }

    public void setShowCaller(String showCaller) {
        this.showCaller = showCaller;
    }

    public void setCallerPackageInclude(String callerPackageInclude) {
        if (callerPackageInclude != null) {
            this.callerPackageInclude = callerPackageInclude;
            this.callerPackageIncludes = callerPackageInclude.split(",");
        }

    }

    public void setCallerPackageExclude(String callerPackageExclude) {
        if (callerPackageExclude != null) {
            this.callerPackageExclude = callerPackageExclude;
            this.callerPackageExcludes = callerPackageExclude.split(",");
        }

    }
}
