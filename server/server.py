import socket


class Server(object):
    def __init__(self, port):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.port = port
        self.host = socket.gethostname()
        self.client_socket = None
        self.client_address = None
        self.data_len_buffer = 16
        self.data_buffer_size = 1024

    def bind(self):
        print("Binding server to " + str(self.host))
        self.socket.bind(("", self.port))
        self.socket.listen()
        print("Listening for connections...")

    def accept(self):
        self.client_socket, self.client_address = self.socket.accept()
        print(str(self.client_address) + " connected.")

    def receive(self):
        """
         Blocking socket.recv(x) call until received all data from client.
         :return: bytes received from client
        """
        data_size = self.recv_data_len()
        self.send_ack()
        # print("Size of data to be received " + str(data_size))
        data = b''
        data_received = 0

        while data_received < data_size:
            buffer = self.client_socket.recv(self.data_buffer_size)
            if not buffer:
                break
            data += buffer
            data_received += len(buffer)

        self.send_ack()
        return data

    def recv_data_len(self):
        """
        Blocking socket.recv(x) call to receive size of data to be received by client.
        :return: integer specifying the size of the data the client wants to send.
        """
        return int(self.client_socket.recv(self.data_len_buffer).decode("utf-8"))

    def send_ack(self):
        """
        Sends a byte to the client socket as an acknowledgement, or confirmation of data being received.
        """
        self.client_socket.send("1".encode())

    def close(self):
        """
        Closes this server socket.
        """
        self.socket.close()
