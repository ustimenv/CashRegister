import tkinter as tk
from functools import partial

import requests

from login import LoginPage


class Client(tk.Tk):
    username = '$$$'
    session_token = None

    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)
        self.geometry('1080x720')
        self.title('Till')

        container = tk.Frame(self)
        container.pack(side="top", fill="both", expand=True)
        container.grid_rowconfigure(0, weight=1)
        container.grid_columnconfigure(0, weight=1)

        self.frames = {}
        for F in (LoginPage,):
            page_name = F.__name__
            frame = F(parent=container, controller=self)
            self.frames[page_name] = frame

            # put all of the pages in the same location;
            # the one on the top of the stacking order
            # will be the one that is visible.
            frame.grid(row=0, column=0, sticky="nsew")

        self.show_frame("LoginPage")

    def show_frame(self, page_name):
        frame = self.frames[page_name]
        frame.tkraise()


if __name__ == "__main__":
    # C = Client()
    # C.mainloop()
    headers = {'Content-type': 'application/json'}

    req = requests.get("http://localhost:8080/cashier/login", json={'username': "V", 'password': "passX"}, headers=headers)
    # req = requests.get("http://localhost:8080/cashier/login", json={'username': "V", 'password': "pass"}, headers=headers)
    print(req.status_code)
