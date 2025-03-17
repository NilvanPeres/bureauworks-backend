package com.example.bureauworks.core.utils;

import static com.example.bureauworks.core.utils.IsNullUtil.isNullOrEmpty;


public class IsFileExtensionValid {
    public static boolean isFileExtensionValid(String fileName) {
        if (isNullOrEmpty(fileName)) {
            return false;
        }
        String validExtension =  ".csv";
        if (fileName.endsWith(validExtension)) {
            return true;
        }
        return false;
    }
}
