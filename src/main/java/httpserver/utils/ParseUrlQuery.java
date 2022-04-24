package httpserver.utils;

import java.util.HashMap;

public class ParseUrlQuery {

    public static String getUrlTarget(String reqTarget){
        if (reqTarget.contains("?")){
            int endIndex = reqTarget.indexOf("?");
            return reqTarget.substring(0, endIndex);
        }
        return reqTarget;
    }


    public static HashMap<String,String> getParameters(String reqTarget){
        HashMap<String, String> params = new HashMap<>();

        if (reqTarget.contains("?")){
            int startIndex = reqTarget.indexOf("?");
            String paramString = reqTarget.substring(startIndex+1);

            String[] splited  = paramString.split("&");
            for (int i = 0; i < splited.length; i++) {
                String[] keyAndValue  = splited[i].split("=");
                params.put(keyAndValue[0], keyAndValue[1]);
            }

            return params;
        }
        return null;
    }

}
