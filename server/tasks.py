"""
Implementation of various Tasks that the server will execute.
"""

import os

import pyautogui


def make_task(data):
    """
    A raw task data string will be in the format of: task_type|task|parameters
    Anything after task_type and task are the parameters, which can be one or many values.
    For example: mouse|move|15|-30
    Where 15 is the x and -30 is the y value for moving the mouse relative to the current position.
    :param data: delimited string data
    :return: Task object
    """
    data = data.split("|")
    task_type = data[0]
    if task_type == "mouse":
        return MouseTask(data)
    elif task_type == "keys":
        return KeyboardTask(data)
    elif task_type == "cmd":
        return CommandTask(data)


class Task(object):
    def __init__(self, data):
        """
        task_type: Type of task e.g. "mouse"
        task: The task itself e.g. "move"
        params: List object containing params for the task e.g [20, -30]
        """
        self.task_type = data[0]  # e.g. "mouse"
        self.task = data[1]  # e.g "press"
        self.params = data[2:]  # e.g ["left"]

    def print(self):
        print(self.task_type)
        print(self.task)
        print(self.params)

    def error(self):
        print("Error executing task: ")
        self.print()


class MouseTask(Task):
    def __init__(self, data):
        Task.__init__(self, data)

    def execute(self):
        if self.task == "move":
            x = int(self.params[0])
            y = int(self.params[1])
            pyautogui.moveRel(x, y)
        elif self.task == "click":
            pyautogui.click(button=self.params[0])
        else:
            self.error()


class KeyboardTask(Task):
    def __init__(self, data):
        Task.__init__(self, data)

    def execute(self):
        if self.task == "press":
            if len(self.params) > 1:  # multiple keys to be pressed at once
                pyautogui.hotkey(*self.params)
            else:
                pyautogui.press(self.params[0])
        else:
            self.error()


class CommandTask(Task):
    def __init__(self, data):
        Task.__init__(self, data)

    def execute(self):
        os.system(self.task)
