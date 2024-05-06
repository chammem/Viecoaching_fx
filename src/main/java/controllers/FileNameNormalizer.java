package controllers;

import java.nio.file.Paths;

public class FileNameNormalizer {

    public static String extractFileNameFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        int lastIndex = imageUrl.lastIndexOf('/');
        if (lastIndex >= 0 && lastIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastIndex + 1); // Returns the filename with extension
        }
        return null;
    }

    public static String normalizeFileName(String imageUrl) {
        String fileName = extractFileNameFromUrl(imageUrl);
        if (fileName != null) {
            // Perform additional normalization if needed (e.g., replace spaces, etc.)
            return fileName;
        }
        return null;
    }
}
