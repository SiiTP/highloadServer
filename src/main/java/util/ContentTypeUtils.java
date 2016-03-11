package util;

public class ContentTypeUtils {
    public static final String CT_DEFAULT = "text/plain";

    public static final String[] EXT_IMAGES = {"png", "jpg", "jpeg", "gif"};
    public static final String[] EXT_TEXT = {"html", "css"};
    public static final String CT_IMAGE = "image/";
    public static final String CT_TEXT = "text/";

    public static final String EXT_JS = "js";
    public static final String CT_JS = "application/javascript";

    public static final String EXT_SWF = "swf";
    public static final String CT_SWF = "application/x-shockwave-flash";

    public static String getContentTypeByExtension(String extention) {
        //images
        boolean imgContains = false;
        for(String value : EXT_IMAGES) {
            if (value.equals(extention)) {
                imgContains = true;
                break;
            }
        }
        if (imgContains) {
            if (extention.equals("jpg")) {
                extention = "jpeg";
            }
            return CT_IMAGE + extention;
        }

        //text
        boolean textContains = false;
        for(String value : EXT_TEXT) {
            if (value.equals(extention)) {
                textContains = true;
                break;
            }
        }
        if (textContains) {
            return CT_TEXT + extention;
        }

        //other
        if (extention.equals(EXT_JS)) {
            return CT_JS;
        }
        if (extention.equals(EXT_SWF)) {
            return CT_SWF;
        }


        return CT_DEFAULT;
    }
}
