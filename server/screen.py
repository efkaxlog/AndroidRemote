import mss
import mss.tools
import numpy


class ScreenSnapper:

    def __init__(self):
        self.sct = mss.mss()
        width = 1920
        height = 1080
        self.monitor = {"top": 0, "left": 0, "width": width, "height": height}

    def screenshot_rgb(self):
        #  todo: detect screen dimensions
        return numpy.array(self.sct.grab(self.monitor), dtype=numpy.uint8)[..., [2, 1, 0]]

    def screenshot_png(self):
        sct_img = self.sct.grab(self.monitor)
        png = mss.tools.to_png(sct_img.rgb, sct_img.size)
        return png
