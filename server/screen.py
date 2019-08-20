import time

import mss
import numpy


class ScreenSnapper:

    def __init__(self):
        pass

    def screenshot(self):
        #  todo: detect screen dimensions
        width = 1920
        height = 1080
        monitor = {"top": 0, "left": 0, "width": width, "height": height}
        with mss.mss() as sct:
            numpy_array = numpy.array(sct.grab(monitor), dtype=numpy.uint8)[..., [2, 1, 0]]
            return numpy_array
