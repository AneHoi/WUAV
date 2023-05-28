package BE;

import javafx.scene.paint.Color;

public class CabelAndColor {
    private String cabelName;
    private Color cabelColor;

    public CabelAndColor(String cabelName, Color cabelColor) {
        this.cabelName = cabelName;
        this.cabelColor = cabelColor;
    }

    public String getCabelName() {
        return cabelName;
    }

    public void setCabelName(String cabelName) {
        this.cabelName = cabelName;
    }

    public Color getCabelColor() {
        return cabelColor;
    }

    public void setCabelColor(Color cabelColor) {
        this.cabelColor = cabelColor;
    }
    public void setCabelColorWithString(String cabelColor) {
        try {
            Color color = Color.valueOf(cabelColor);
            this.cabelColor = color;
        } catch (Exception e) {
            System.out.println("This was not possible");
        }
    }

}
