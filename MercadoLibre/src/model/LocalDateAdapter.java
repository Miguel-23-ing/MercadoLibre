package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    @Override
    public void write(JsonWriter out, LocalDate date) throws IOException {
        if (date != null) {
            out.value(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            out.nullValue();
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String dateString = in.nextString();
        if (dateString != null && !dateString.isEmpty()) {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            return null;
        }
    }
}