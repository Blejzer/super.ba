package info.bdslab.android.asuper.Utils;

/**
 * Created by nikola on 07/10/2017.
 */

public class Config {
    String CLIENT_ID = "5952144f7e664a87a18c158b_2fpcotn1f8n4so8oo8s4gwg8ogsgk8g48oksc044s0o4k0kow0";
    String CLIENT_SECRET = "34vrb64rxx8g8kc8s4ck8s4wocc4kcgkws4cookcocog0k8gcw";
    String USERNAME = "iOSApp@super.ba";
    String PASSWORD= "thereisnopass";

    String SITE = "https://super.ba/";
    String PATHTOKEN = "oauth/v2/token?";
    String PATHAPIVERSION = "api";
    String PATHARTICLES = "api/v1/articles";
    String PATHSOURCES = "api/v1/sources";
    String CATEGORY = "?category=BiH";
    String OFFSET = "?offset=";
    String LIMIT = "?limit=10";
    String FILTERS = "&filters=";


    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getSITE() {
        return SITE;
    }

    public String getPATHTOKEN() {
        return PATHTOKEN;
    }

    public String getPATHAPIVERSION() {
        return PATHAPIVERSION;
    }

    public String getPATHARTICLES() {
        return PATHARTICLES;
    }

    public String getPATHSOURCES() {
        return PATHSOURCES;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public String getOFFSET() {
        return OFFSET;
    }

    public String getLIMIT() {
        return LIMIT;
    }

    public String getFILTERS() {
        return FILTERS;
    }
}

