import tkinter as tk

import requests

from checkout_screen import CheckoutScreen
from login_screen import LoginScreen


class Client(tk.Tk):
    username = None
    session_token = None
    server_base_url = "http://localhost:8080"
    cashier_url = "http://localhost:8080/cashier"
    checkout_url = "http://localhost:8080/checkout"

    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)
        self.geometry('1080x720')
        self.title('Till')

        container = tk.Frame(self)
        container.pack(side="top", fill="both", expand=True)
        container.grid_rowconfigure(0, weight=1)
        container.grid_columnconfigure(0, weight=1)

        self.frames = {}
        for F in (LoginScreen, CheckoutScreen):
            page_name = F.__name__
            frame = F(parent=container, controller=self)
            self.frames[page_name] = frame

            # put all of the pages in the same location;
            # the one on the top of the stacking order
            # will be the one that is visible.
            frame.grid(row=0, column=0, sticky="nsew")

        self.show_frame("LoginScreen")

    def show_frame(self, page_name):
        frame = self.frames[page_name]
        if hasattr(frame, 'prepare'):
            frame.prepare()
        frame.tkraise()


if __name__ == "__main__":
    C = Client()
    C.mainloop()
