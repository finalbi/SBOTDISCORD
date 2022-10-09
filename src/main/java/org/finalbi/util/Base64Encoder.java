package org.finalbi.util;

import java.io.IOException;
import java.io.OutputStream;

public class Base64Encoder extends OutputStream {
    private Appendable out;
    private int p = 0, tmp = 0;
    private static final char[] charMap =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();


    public Base64Encoder (Appendable out){
        this.out = java.util.Objects.requireNonNull(out);
    }

    @Override
    public void write(int b) throws IOException {
        b &= 0xFF;
        if (p == 0){
            out.append(charMap[b >> 2]);
            tmp = (b & 0x3) << 4;
            p = 1;
        } else if (p == 1) {
            out.append(charMap[tmp | (b >> 4)]);
            tmp = (b & 0xF) << 2;
            p = 2;
        }else {
            out.append(charMap[tmp | (b >> 4)]);
            out.append(charMap[b & 0x3F]);
            p = 0;
        }
    }

    @Override
    public void close() throws IOException {
        if (p != 0){
            out.append(charMap[tmp]);
            if (p == 1) out.append('=').append('=');
            if (p == 2) out.append('=');
        }
        out = null;
    }
}
