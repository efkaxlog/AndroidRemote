import socket
import threading


class ServerDiscovery(object):
    """
    Used to identify a server for the client. A client will broadcast to all servers on the network,
    and these objects reply with their ip address and other information such as host machine name. This way the client
    will find any servers on the network and secondly the client does not have to know the specific ip address of a
    server to connect as this will be used to get it.
    """

    def __init__(self, port):
        self.port = port
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(("", port))
        self.host = socket.gethostname()
        self.client_request_buffer = 128  # max amount of bytes to receive from the client for server info request

        thread = threading.Thread(target=self.run, args=())
        thread.daemon = True
        thread.start()

    def run(self):
        while True:
            data, addr = self.socket.recvfrom(self.client_request_buffer)
            data = data.decode()
            print(data + " from " + str(addr))
            if data == "server_info_request":
                response = self.host.encode()
            else:
                response = "Bad request.".encode()
            self.socket.sendto(response, addr)
