package pivixproject.demo.pageModel;

public class pageData {

    private String rawData;
    private String originalSizeUrl;
    private String regularSizeUrl;

    public String getRegularSizeUrl() {
        return regularSizeUrl;
    }

    public void setRegularSizeUrl(String regularSizeUrl) {
        this.regularSizeUrl = regularSizeUrl;
    }

    public String getOriginalSizeUrl() {
        return originalSizeUrl;
    }

    public void setOriginalSizeUrl(String originalSizeUrl) {
        this.originalSizeUrl = originalSizeUrl;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
