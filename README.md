# Chat Application

## Overview

This project implements a simple chat application using Java that demonstrates communication between a client and a server. The application uses Java's Socket API to enable real-time messaging. The server listens for client connections, and the client connects to the server to send and receive messages.

## Features

- **Real-time Communication**: Enables two-way communication between the client and server.
- **GUI**: Both client and server applications feature a user-friendly graphical user interface (GUI) built using Swing.
- **Message Display**: Displays messages sent and received by both client and server in a text area.
- **Exit Condition**: Both the client and server can terminate the chat by typing `exit`.

## Code Explanation

### Client Class
- **Socket**: The client connects to the server via a socket on localhost (127.0.0.1) at port 7777.
- **GUI**: The JFrame displays the message area and input field.
- **Message Handling**: The client listens for key events (pressing Enter) to send messages to the server.

### Server Class
- **ServerSocket**: The server listens for incoming connections from clients on port 7777.
- **GUI**: The JFrame displays the message area and input field, similar to the client.
- **Message Handling**: The server listens for incoming messages from the client and sends messages back to the client.

## Message Exchange

Both the server and client use a BufferedReader and PrintWriter for reading from and writing to the socket. Messages are displayed in the message area for each side.
