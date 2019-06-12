import tasks
from server import Server
from server_discovery import ServerDiscovery

PORT = 9999


def main():
    server_discovery = ServerDiscovery(PORT)  # Starts a new thread with UDP socket.
    server = Server(PORT)
    server.bind()
    while True:
        server.accept()
        while True:
            data = server.receive().decode()
            print(data)
            if data == "close":
                server.send_ack()
                break
            task = tasks.make_task(data)
            if task is not None:
                task.execute()


if __name__ == "__main__":
    main()
