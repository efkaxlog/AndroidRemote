import tasks
from server import Server
from server_discovery import ServerDiscovery
import screen

discovery_port = 9997  # for server discovery
send_port = 9998  # for receiver socket
recv_port = 9999  # for sender socket



def main():
    server_discovery = ServerDiscovery(discovery_port)  # Starts a new thread with UDP socket.
    server = Server(recv_port, send_port)
    server.bind()
    while True:
        server.accept()
        print(server.client_recv_socket)
        print(server.client_sender_socket)
        while True:
            data = server.receive().decode()
            print(data)
            if data == "close":
                server.close_client()
                break
            elif data == "ping":
                server.ping_response()
            elif data == "request_screenshot":
                data = screen.ScreenSnapper().screenshot().tobytes()
                server.send_data(data, "screenshot")
            else:
                task = tasks.make_task(data)
                if task is not None:
                    task.execute()


if __name__ == "__main__":
    main()
