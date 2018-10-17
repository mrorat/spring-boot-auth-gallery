//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quasar.files;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamWithSize implements Closeable {
    private InputStream inputStream;
    private long size;

    InputStreamWithSize(InputStream inputStream, long l) {
        this.inputStream = inputStream;
        this.size = l;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public long getSize() {
        return this.size;
    }

    public void close() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
        }

    }
}
