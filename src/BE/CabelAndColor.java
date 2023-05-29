package BE;

import javafx.scene.paint.Color;

public class CabelAndColor {
    private String cabelName;
    private String cabelColor;

    public CabelAndColor(String cabelName, String cabelColor) {
        this.cabelName = cabelName;
        this.cabelColor = cabelColor;
    }

    public String getCabelName() {
        return cabelName;
    }

    public void setCabelName(String cabelName) {
        this.cabelName = cabelName;
    }

    public String getCabelColor() {
        return cabelColor;
    }

    public void setCabelColor(String cabelColor) {
        this.cabelColor = cabelColor;
    }
}
