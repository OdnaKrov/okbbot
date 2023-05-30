package com.ok.okbot.service.command;

public interface ImageContentService {
    default int checkSum(byte[] array) {
        int result = 0;
        for (final byte v : array) {
            result += v;
        }
        return result;
    }
}
