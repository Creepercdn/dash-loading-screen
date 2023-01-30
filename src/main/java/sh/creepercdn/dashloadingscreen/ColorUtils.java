package sh.creepercdn.dashloadingscreen;

// https://github.com/TeamQuantumFusion/DashLoader/blob/fabric-1.18/src/main/java/dev/quantumfusion/dashloader/client/UIColors.java

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorUtils {
    public static Color BACKGROUND_COLOR = null;
    public static Color PROGRESS_LANE_COLOR = null;
    public static Color TEXT_COLOR = null;

    public static Map<String, Color> COLORS = new HashMap<>();
    public static Color[] PROGRESS_COLORS = null;

    public static void loadConfig(ModConfig config) {
        COLORS.clear();
        config.color.colorVariables.forEach((s, s2) -> COLORS.put(s, Color.decode(s2)));

        final String[] progressColors = config.color.progressColors;
        if (progressColors.length == 0) {
            throw new RuntimeException("Progress Colors length is 0");
        }
        PROGRESS_COLORS = new Color[progressColors.length];
        for (int i = 0; i < progressColors.length; i++) {
            PROGRESS_COLORS[i] = parseColor(progressColors[i]);
        }

        BACKGROUND_COLOR = parseColor(config.color.backgroundColor);
        PROGRESS_LANE_COLOR = parseColor(config.color.progressTrackColor);
        TEXT_COLOR = parseColor(config.color.foregroundColor);
    }

    public static Color parseColor(String str) {
        if (COLORS.containsKey(str.toLowerCase())) {
            return COLORS.get(str.toLowerCase());
        } else {
            try {
                return Color.decode(str.toUpperCase());
            } catch (NumberFormatException formatException) {
                return Color.MAGENTA;
            }
        }
    }


    public static Color getProgressColor(double progress) {
        return mix(progress, PROGRESS_COLORS);
    }

    public static Color mix(double pos, Color... colors) {
        if (colors.length == 1) {
            return colors[0];
        }
        pos = Math.min(1, Math.max(0, pos));
        int breaks = colors.length - 1;
        if (pos == 1) {
            return colors[breaks];
        }
        int colorPos = (int) Math.floor(pos * (breaks));
        final double step = 1d / (breaks);
        double localRatio = (pos % step) * breaks;
        return blend(colors[colorPos], colors[colorPos + 1], localRatio);
    }

    public static Color blend(Color i1, Color i2, double ratio) {
        if (ratio > 1f) {
            ratio = 1f;
        } else if (ratio < 0f) {
            ratio = 0f;
        }
        double iRatio = 1.0f - ratio;

        int a = (int) ((i1.getAlpha() * iRatio) + (i2.getAlpha() * ratio));
        int r = (int) ((i1.getRed() * iRatio) + (i2.getRed() * ratio));
        int g = (int) ((i1.getGreen() * iRatio) + (i2.getGreen() * ratio));
        int b = (int) ((i1.getBlue() * iRatio) + (i2.getBlue() * ratio));

        return new Color(r, g, b, a);
    }

    public static void print(int color) {
        int r2 = ((color & 0xff0000) >> 16);
        int g2 = ((color & 0xff00) >> 8);
        int b2 = (color & 0xff);
        System.out.println("\033[48;2;" + r2 + ";" + g2 + ";" + b2 + "m");
    }
}