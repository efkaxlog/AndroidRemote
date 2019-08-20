import socket
import struct

class Server(object):
    def __init__(self, recv_port, send_port):
        self.recv_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sender_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client_recv_socket = None
        self.client_sender_socket = None
        self.client_address = None
        self.recv_port = recv_port
        self.send_port = send_port
        self.host = socket.gethostname()
        self.data_buffer_size = 1024

    def bind(self):
        print("Binding server to " + str(self.host))
        self.recv_socket.bind(("", self.recv_port))
        self.recv_socket.listen()
        self.sender_socket.bind(("", self.send_port))
        self.sender_socket.listen()
        print("Listening for connections...")

    def accept(self):
        self.client_recv_socket, self.client_address = self.recv_socket.accept()
        print("Recv socket connected ")
        self.client_sender_socket, self.client_address = self.sender_socket.accept()
        print("Sender socket connected")
        print(str(self.client_address) + " connected.")

    def receive(self):
        """
         Blocking socket.recv(x) call until received all data from client.
         :return: bytes received from client
        """
        raw_msglen = self.recv_all(self.client_recv_socket, 4)  # 4 bytes int
        if not raw_msglen:
            return None
        self.send_ack(self.client_recv_socket)
        msglen = struct.unpack(">I", raw_msglen)[0]
        msg = self.recv_all(self.client_recv_socket, msglen)
        self.send_ack(self.client_recv_socket)
        return msg

    def recv_all(self, sock, length):
        data = b''
        while len(data) < length:
            buffer = sock.recv(self.data_buffer_size)
            if not buffer:
                break
            data += buffer
        return data

    def send_ack(self, sock):
        """
        Sends a byte to the client socket as an acknowledgement, or confirmation of data being received.
        """
        sock.sendall(b"1")

    def recv_ack(self, sock):
        sock.recv(1)

    def send_data(self, data, data_type):
        data_type = data_type.encode()
        data = struct.pack(">I", len(data)) + struct.pack(">i", len(data_type)) + data_type + data
        self.client_sender_socket.sendall(data)
        self.recv_ack(self.client_sender_socket)

    def ping_response(self):
        self.send_ack(self.client_recv_socket)

    def close_client(self):
        """
        Closes this server socket.
        """
        print("Closing")
        self.send_ack(self.client_sender_socket)
        self.client_recv_socket.close()
        self.client_sender_socket.close()
