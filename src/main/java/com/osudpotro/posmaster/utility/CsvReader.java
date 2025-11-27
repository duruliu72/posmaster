package com.osudpotro.posmaster.utility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReader {

    public List<String[]> readCSV(MultipartFile file) {

        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {

                // Skip empty lines
                if (line.trim().isEmpty()) continue;

                String[] columns = line.split(",");
                rows.add(columns);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }

        return rows;
    }
}