/*
 * Copyright (c) 2002-2018, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.terminal.impl;

import org.jline.terminal.Attributes;
import org.jline.terminal.Attributes.ControlChar;
import org.jline.terminal.Size;
import org.jline.utils.NonBlocking;
import org.jline.utils.NonBlockingInputStream;
import org.jline.utils.NonBlockingReader;

import java.io.*;
import java.nio.charset.Charset;

public class DumbTerminal extends AbstractTerminal {

    private final NonBlockingInputStream input;
    private final OutputStream output;
    private final NonBlockingReader reader;
    private final PrintWriter writer;
    private final Attributes attributes;
    private final Size size;

    public DumbTerminal(String name, String type, InputStream in, OutputStream out, Charset encoding, SignalHandler signalHandler) throws IOException {
        super(name, type, encoding, signalHandler);
        NonBlockingInputStream nbis = NonBlocking.nonBlocking(getName(), in);
        this.input = new NonBlockingInputStream() {
            @Override
            public int read(long timeout, boolean isPeek) throws IOException {
                for (;;) {
                    int c = nbis.read(timeout, isPeek);
                    if (attributes.getLocalFlag(Attributes.LocalFlag.ISIG)) {
                        if (c == attributes.getControlChar(ControlChar.VINTR)) {
                            raise(Signal.INT);
                            continue;
                        } else if (c == attributes.getControlChar(ControlChar.VQUIT)) {
                            raise(Signal.QUIT);
                            continue;
                        } else if (c == attributes.getControlChar(ControlChar.VSUSP)) {
                            raise(Signal.TSTP);
                            continue;
                        } else if (c == attributes.getControlChar(ControlChar.VSTATUS)) {
                            raise(Signal.INFO);
                            continue;
                        }
                    }
                    if (c == '\r') {
                        if (attributes.getInputFlag(Attributes.InputFlag.IGNCR)) {
                            continue;
                        }
                        if (attributes.getInputFlag(Attributes.InputFlag.ICRNL)) {
                            c = '\n';
                        }
                    } else if (c == '\n' && attributes.getInputFlag(Attributes.InputFlag.INLCR)) {
                        c = '\r';
                    }
                    return c;
                }
            }
        };
        this.output = out;
        this.reader = NonBlocking.nonBlocking(getName(), input, encoding());
        this.writer = new PrintWriter(new OutputStreamWriter(output, encoding()));
        this.attributes = new Attributes();
        this.attributes.setControlChar(ControlChar.VERASE,  (char) 127);
        this.attributes.setControlChar(ControlChar.VWERASE, (char) 23);
        this.attributes.setControlChar(ControlChar.VKILL,   (char) 21);
        this.attributes.setControlChar(ControlChar.VLNEXT,  (char) 22);
        this.size = new Size();
        parseInfoCmp();
    }

    public NonBlockingReader reader() {
        return reader;
    }

    public PrintWriter writer() {
        return writer;
    }

    @Override
    public InputStream input() {
        return input;
    }

    @Override
    public OutputStream output() {
        return output;
    }

    public Attributes getAttributes() {
        Attributes attr = new Attributes();
        attr.copy(attributes);
        return attr;
    }

    public void setAttributes(Attributes attr) {
        attributes.copy(attr);
    }

    public Size getSize() {
        Size sz = new Size();
        sz.copy(size);
        return sz;
    }

    public void setSize(Size sz) {
        size.copy(sz);
    }

}
