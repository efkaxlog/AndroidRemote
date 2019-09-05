import mss
import mss.tools
import numpy
import io
import pyautogui

from PIL import Image


def scale_sct(sct, scale):
    """
    :param sct: mss Screenshot object
    :param scale: (width, height) tuple
    :return: PIL Image object
    """
    image = Image.frombytes("RGB", sct.size, sct.bgra, "raw", "BGRX")
    image.resize(scale, Image.NEAREST)
    return image

class ScreenSnapper:

    def __init__(self):
        self.sct = mss.mss()
        width, height = pyautogui.size()
        # To what dimensions the screenshot will be scaled down to
        self.scale = (320, 180)
        self.monitor = {"top": 0, "left": 0, "width": width, "height": height}

    def screenshot_rgb(self):
        #  todo: detect screen dimensions
        return numpy.array(self.sct.grab(self.monitor), dtype=numpy.uint8)[..., [2, 1, 0]].tobytes()

    def screenshot_png(self):
        sct = self.sct.grab(self.monitor)
        sct_scaled = scale_sct(sct, self.scale)
        with io.BytesIO() as output:
            sct_scaled.save(output, "png")
            return output.getvalue()
