package br.eti.kinoshita.colors_from_json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static class Entry {
        @JsonAlias("FileName")
        private String fileName;
        @JsonAlias("Level")
        private Integer level;
        @JsonAlias("Opacity")
        private Integer opacity;
        @JsonAlias("Category")
        private String category;
        @JsonAlias("Pattern")
        private String pattern;
        @JsonAlias("IsExclusionZone")
        private Integer isExclusionZone;
        @JsonAlias("IsInclusionZone")
        private Integer isInclusioinZone;
        @JsonAlias("AppliesTo")
        private Integer appliesTo;
        @JsonAlias("Proximity")
        private Integer proximity;
        @JsonAlias("AudibleAlarm")
        private Integer audibleAlarm;
        @JsonAlias("AllowOverride")
        private Integer allowOverride;
        @JsonAlias("ShapeType")
        private Integer shapeType;
        @JsonAlias("ShapeRecords")
        private Integer shapeRecords;
        @JsonAlias("RouteWidth")
        private Integer routeWidth;
        @JsonAlias("LabelField")
        private String labelField;
        @JsonAlias("FieldNames")
        private String fieldNames;
        @JsonAlias("Comment")
        private String comment;
        @JsonAlias("isShowOnMap")
        private Integer isShowOnMap;
        @JsonAlias("LabelColor")
        private Integer labelColor;
        @JsonAlias("ID")
        private Integer id;

        public Entry() {
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Integer getOpacity() {
            return opacity;
        }

        public void setOpacity(Integer opacity) {
            this.opacity = opacity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public Integer getIsExclusionZone() {
            return isExclusionZone;
        }

        public void setIsExclusionZone(Integer isExclusionZone) {
            this.isExclusionZone = isExclusionZone;
        }

        public Integer getIsInclusioinZone() {
            return isInclusioinZone;
        }

        public void setIsInclusioinZone(Integer isInclusioinZone) {
            this.isInclusioinZone = isInclusioinZone;
        }

        public Integer getAppliesTo() {
            return appliesTo;
        }

        public void setAppliesTo(Integer appliesTo) {
            this.appliesTo = appliesTo;
        }

        public Integer getProximity() {
            return proximity;
        }

        public void setProximity(Integer proximity) {
            this.proximity = proximity;
        }

        public Integer getAudibleAlarm() {
            return audibleAlarm;
        }

        public void setAudibleAlarm(Integer audibleAlarm) {
            this.audibleAlarm = audibleAlarm;
        }

        public Integer getAllowOverride() {
            return allowOverride;
        }

        public void setAllowOverride(Integer allowOverride) {
            this.allowOverride = allowOverride;
        }

        public Integer getShapeType() {
            return shapeType;
        }

        public void setShapeType(Integer shapeType) {
            this.shapeType = shapeType;
        }

        public Integer getShapeRecords() {
            return shapeRecords;
        }

        public void setShapeRecords(Integer shapeRecords) {
            this.shapeRecords = shapeRecords;
        }

        public Integer getRouteWidth() {
            return routeWidth;
        }

        public void setRouteWidth(Integer routeWidth) {
            this.routeWidth = routeWidth;
        }

        public String getLabelField() {
            return labelField;
        }

        public void setLabelField(String labelField) {
            this.labelField = labelField;
        }

        public String getFieldNames() {
            return fieldNames;
        }

        public void setFieldNames(String fieldNames) {
            this.fieldNames = fieldNames;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getIsShowOnMap() {
            return isShowOnMap;
        }

        public void setIsShowOnMap(Integer isShowOnMap) {
            this.isShowOnMap = isShowOnMap;
        }

        public Integer getLabelColor() {
            return labelColor;
        }

        public void setLabelColor(Integer labelColor) {
            this.labelColor = labelColor;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

    public static String normalizeColor (String s, boolean exclusionZone) {
        if (exclusionZone) {
            // s.length() == 6
            // return normalizeColor(s + "00");
            // No idea about this one, honestly. The best guess I could make is that exclusion zones are red?
            return "FF0000FF";
        }
        // an eight length color has the alpha color at the beginning, whereas it must be at the end for browsers?
        if (s.length() == 8) {
            return s.substring(2) + s.substring(0, 2);
        }
        // a two length color appears to be missing left padding zeroes, and let's throwing in the alpha channel to have everything with 8 digit?
        if (s.length() == 2) {
            return "0000" + s + "FF";
        }
        return s;
    }
    
    public static void main(String[] args) throws IOException, DecoderException {
        File jsonFile = new File(Main.class.getResource("/MAP.json")
                .getFile());
        String jsonInput = FileUtils.readFileToString(jsonFile, Charset.defaultCharset());
        ObjectMapper mapper = new ObjectMapper();
        List<Entry> entries = mapper.readValue(jsonInput, mapper.getTypeFactory()
                .constructCollectionType(List.class, Entry.class));
        try (FileWriter fos = new FileWriter(new File("/tmp/report.html"))) {
            fos.write("<table border='1' width='100%'>"
                    + "<thead>"
                    + "<tr>"
                    + "<th>Shapefile</th><th>Type</th><th>Color</th><th>Image</th><th>Label color</th>"
                    + "</tr>"
                    + "</thead>"
                    + "<tbody>");
            for (Entry entry : entries) {
                String shapeFile = entry.getFileName();
                String type = entry.getCategory() == null ? "" : entry.getCategory().split("\\|")[0];
                String categoryColor = entry.getCategory() == null ? "" : Integer.toHexString(Integer.parseInt(entry.getCategory().split("\\|")[1]));
                String categoryColorTd = "<td></td>";
                if (type != null && !type.equals("")) {
                    if (!type.equals("User defined")) {
                        // This is strange... in a 8 digit hex, I think the transparency was set by the last two digits... that's
                        // how the browser interprets it too. But here it looks like if we move the two first digits to the end
                        // then we get the right color/transparency? Except not for User defined?
                        categoryColor = normalizeColor(categoryColor, entry.isExclusionZone != 0);
                        categoryColorTd = "<td style='background: #" + categoryColor + "'>#" + categoryColor + "</td>";
                    } else {
                        // Now comes to interesting part... the Category contains three numbers. The image contains two
                        // images. Well, we can try the following:
                        // 1. Assume the first number is the base color, not used.
                        // 2. And adding up the first and the second color, now we get a derived color. Used in the first part of the image.
                        // 3. You guessed right!
                        int baseColor = Integer.parseInt(entry.getCategory().split("\\|")[1]);
                        int secondColor = Integer.parseInt(entry.getCategory().split("\\|")[2]);
                        int thirdColor = Integer.parseInt(entry.getCategory().split("\\|")[3]);
                        
                        String leftColor = normalizeColor(Integer.toHexString(baseColor + secondColor + thirdColor), entry.isExclusionZone != 0);
                        String rightColor = normalizeColor(Integer.toHexString(baseColor + secondColor), entry.isExclusionZone != 0);
                        
                        categoryColorTd = "<td><table border='0' width='100%'>"
                                + "<tr>"
                                + "<td width='50%' style='background: #" + leftColor + "'>#" + leftColor + "</td>"
                                + "<td width='50%' style='background: #" + rightColor + "'>#" + rightColor + "</td>"
                                + "</tr>"
                                + "</table></td>";
                    }
                }
                String png = entry.getPattern() != null ? Base64.encodeBase64String(Hex.decodeHex(entry.getPattern().replaceAll("'",  ""))) : "";
                String labelColor = entry.getLabelColor() == null ? "" : Integer.toHexString(entry.getLabelColor());
                fos.write("<tr>"
                        + "<td>" + shapeFile + "</td>"
                        + "<td>" + type + "</td>"
                        + categoryColorTd
                        + "<td><img src='data:image/png;base64," + png + "' /></td>"
                        + "<td style='background: #" + labelColor + "'>#" + labelColor + "</td>"
                        + "</tr>");
            }
            fos.write("</tbody>"
                    + "</table>");
        }
    }
}
