package interfaces;

import java.io.IOException;

public interface Serverable {

    void initServer() throws IOException;

    void initView();
}
