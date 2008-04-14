/*
 * Copyright (C) 2006-2008 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing"
 */

package org.alfresco.jlan.smb.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Socket Packet Handler Class
 * 
 * <p>Provides the base class for Java Socket based packet handler implementations.
 *
 * @author gkspencer
 */
public abstract class SocketPacketHandler extends PacketHandler {

  // Socket that this session is using.

  private Socket m_socket;

  // Input/output streams for receiving/sending SMB requests.

  private DataInputStream m_in;
  private DataOutputStream m_out;

  /**
   * Class constructor
   * 
   * @param sock Socket
   * @param typ int
   * @param name String
   * @param shortName String
   * @exception IOException   If a network error occurs
   */
  public SocketPacketHandler(Socket sock, int typ, String name, String shortName)
    throws IOException {
    
    super(typ, name, shortName);
    
    m_socket = sock;

    //  Set socket options
    
    sock.setTcpNoDelay(true);
    
    //  Open the input/output streams
    
    m_in  = new DataInputStream(m_socket.getInputStream());
    m_out = new DataOutputStream(m_socket.getOutputStream());
    
    // Set the remote address
    
    setRemoteAddress(m_socket.getInetAddress());
  }
  
  /**
   * Return the count of available bytes in the receive input stream
   * 
   * @return int
   * @exception IOException   If a network error occurs.
   */
  public int availableBytes()
    throws IOException {
    if ( m_in != null)
      return m_in.available();
    return 0;
  }
  
  /**
   * Read a packet
   * 
   * @param pkt byte[]
   * @param off int
   * @param len int
   * @return int
   * @exception IOException   If a network error occurs.
   */
  public int readPacket(byte[] pkt, int off, int len)
    throws IOException {
      
    //  Read a packet of data
    
    if ( m_in != null)
      return m_in.read(pkt,off,len);
    return 0;
  }
  
  /**
   * Send an SMB request packet
   * 
   * @param pkt byte[]
   * @param off int
   * @param len int
   * @exception IOException   If a network error occurs.
   */
  public void writePacket(byte[] pkt, int off, int len)
    throws IOException {

    //  Output the raw packet
    
    if ( m_out != null)
      m_out.write(pkt, off, len);
  }

  /**
   * Flush the output socket
   * 
   * @exception IOException   If a network error occurs
   */
  public void flushPacket()
    throws IOException {
    if ( m_out != null)
      m_out.flush();    
  }
  
  /**
   * Close the protocol handler
   */
  public void closeHandler() {

    //  Close the input stream
    
    if ( m_in != null) {
      try {
        m_in.close();
      }
      catch (Exception ex) {
      }
      m_in = null;
    }
    
    //  Close the output stream
    
    if ( m_out != null) {
      try {
        m_out.close();
      }
      catch (Exception ex) {
      }
      m_out = null;
    }

    //  Close the socket
    
    if (m_socket != null) {
      try {
        m_socket.close();
      }
      catch (Exception ex) {
      }
      m_socket = null;
    }
  }
}
