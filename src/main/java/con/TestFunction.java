package con;

import java.security.PrivateKey;

/**
 * Created by lxq on 15-5-17.
 */
public enum TestFunction {

    ZDT1("ZDT1"), ZDT2("ZDT2"), ZDT3("ZDT3"), ZDT4("ZDT4");

    private String name;

    public String getName() {
        return name;
    }

    TestFunction(String name) {
        this.name = name;
    }
}
