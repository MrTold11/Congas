/*
 * Copyright (c) 2002-2016, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.terminal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Pty extends Closeable {

    InputStream getMasterInput() throws IOException;

    OutputStream getMasterOutput() throws IOException;

    InputStream getSlaveInput() throws IOException;

    OutputStream getSlaveOutput() throws IOException;

    Attributes getAttr() throws IOException;

    void setAttr(Attributes attr) throws IOException;

    Size getSize() throws IOException;

    void setSize(Size size) throws IOException;

}
