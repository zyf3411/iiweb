package com.sunnyz.iiwebapi.util;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EnumService {

    private final Pattern ENUM_PATTERN = Pattern.compile("^enum:(.*?)=(.*?)$");

    public void processEnumFields(List<Map> rows) {
        if (rows == null || rows.size() == 0) {
            return;
        }

        List<String[]> enumFields = new ArrayList<>();
        for (Map.Entry kv : (Set<Map.Entry>) rows.get(0).entrySet()) {
            Object v = kv.getValue();
            if (v != null && v instanceof String && ((String) v).startsWith("enum:")) {
                Matcher matcher = ENUM_PATTERN.matcher((String) v);
                if (matcher.find()) {
                    enumFields.add(new String[]{(String) kv.getKey(), matcher.group(1), matcher.group(2)});
                }
            }
        }
        if (enumFields.size() == 0) {
            return;
        }

        //eg: 'enum:status=USER_STATUS' as `status_desc`
        String transKey, fieldKey, enumKey;
        for (Map row : rows) {
            for (String[] enumField : enumFields) {
                //status_desc
                transKey = enumField[0];
                //status
                fieldKey = enumField[1];
                //USER_STATUS
                enumKey = enumField[2];
                Object fieldValue = row.get(fieldKey);
                if (fieldValue != null) {
                    String transValue = "todo"; // lookupEnumDesc(enumKey + "_" + fieldValue);
                    row.put(transKey, transValue);
                }
            }
        }
    }
}
