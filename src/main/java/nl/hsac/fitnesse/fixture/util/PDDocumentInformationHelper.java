package nl.hsac.fitnesse.fixture.util;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.DateConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Helper to deal with PDDocumentInformation objects.
 */
public class PDDocumentInformationHelper {
    public Map<String, Object> convertToMap(PDDocumentInformation documentInformation) {
        return documentInformation.getCOSObject()
                .entrySet().stream()
                .collect(StreamUtil.toLinkedMap(e -> e.getKey().getName(), e -> getValue(e)));
    }

    protected Object getValue(Map.Entry<COSName, COSBase> entry) {
        Object value = entry.getValue();
        if (value instanceof COSString) {
            COSString cosString = (COSString) value;
            COSName key = entry.getKey();
            Object parsedValue = parseValue(key, cosString);
            if (parsedValue == null) {
                value = cosString.getString();
            } else {
                value = parsedValue;
            }
        }
        return value;
    }

    protected Object parseValue(COSName key, COSString cosString) {
        Object parsedValue = null;
        if (key.getName().endsWith("Date")) {
            Calendar calendar = DateConverter.toCalendar(cosString);
            if (calendar != null) {
                parsedValue = formatCalender(calendar);
            }
        }
        return parsedValue;
    }

    protected Object formatCalender(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(calendar.getTime());
    }
}
