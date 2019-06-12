"""
This module is only used for testing functionality.
"""

import socket


def recv_ack(socket):
    socket.recv(1)


actions = {"1": "mouse|move|20|20",
           "2": "cmd|shutdown|0"}

PORT = 9999
timeout = 2  # Timeout in seconds for socket blocking calls


def find_server():
    """
    :return: found server address
    """
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as client_socket:
        client_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        client_socket.settimeout(2)
        data = "server_info_request".encode()
        client_socket.sendto(data, ("255.255.255.255", PORT))
        while True:
            try:
                reply, addr = client_socket.recvfrom(1024)
                print("Found server.")
                print("Hostname: " + reply.decode())
                print("IP: " + addr[0] + "\n")
            except socket.timeout:
                print("Server search complete.")
                break

    return addr[0]


address = find_server()

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((address, PORT))
    while True:
        choice = input()
        action = actions[choice]
        data = action.encode()
        data_size = str(len(data)).encode()
        print(data)

        s.sendall(data_size)
        recv_ack(s)
        s.sendall(data)
        recv_ack(s)

