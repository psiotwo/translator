package cz.sio2.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class OrderedProperties {

    private List<String> keyList = new ArrayList<String>();
    private Properties p = new Properties();

    public synchronized void load(InputStream inStream) throws IOException {
        final BufferedReader r = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));

        String line;

        try {
            while ((line = r.readLine()) != null) {
                if (line.trim().startsWith("#") || line.trim().isEmpty()) {
                    keyList.add(line);
                } else {
                    int i = line.indexOf("=");
                    String key = line.substring(0, i);
                    String value = line.substring(i + 1);
                    put(key, value);
                }
            }
        } finally {
            r.close();
        }
    }

    public synchronized void store(OutputStream inStream, String comments) throws IOException {      
        final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(inStream, "UTF-8"));

        if (comments != null && !comments.isEmpty()) {
            w.write("#" + comments);
            w.newLine();
            w.newLine();
        }

        try {
            for (String s : keyList) {
                if (s.startsWith("#") || s.isEmpty()) {
                    w.write(s);
                } else {
                    w.write(s + "=" + new String(p.getProperty(s).getBytes("UTF-8"),"UTF-8"));
                }
                w.newLine();
            }
        } finally {
            w.close();
        }
    }

    public Set<Object> keySet() {
        return p.keySet();
    }

    public String getProperty(String key) {
        return p.getProperty(key);
    }

    public Object put(Object k, Object v) {
        if (!p.containsKey(k)) {
            keyList.add(k + "");
        }

        return p.put(k, v);
    }

    public Object get(Object k) {
        return p.get(k);
    }

    public boolean containsKey(Object k) {
        return p.containsKey(k);
    }

    public int size() {
        return p.size();
    }
}
