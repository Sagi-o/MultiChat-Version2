package interfaces;

import java.io.IOException;
import java.net.UnknownHostException;

public interface Serverable {

    void initServer() throws IOException;

    void initView() throws UnknownHostException;
}
