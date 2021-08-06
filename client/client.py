import tkinter as tk

import requests

from checkout_screen import CheckoutScreen
from login_screen import LoginScreen


class Client(tk.Tk):
    username = None
    _session_token = None

    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)
        self.geometry('1920x1080')
        self.title('Till')
        self.route = "http://localhost:8080"
        self.container = tk.Frame(self)
        self.container.pack(side="top", fill="both", expand=True)
        self.container.grid_rowconfigure(0, weight=1)
        self.container.grid_columnconfigure(0, weight=1)

        self.frames = {}
        for F in (LoginScreen, CheckoutScreen):
            page_name = F.__name__
            frame = F(parent=self.container, controller=self)
            self.frames[page_name] = frame
            # stack the frames on top of each other, the raised one will be the one visible
            frame.grid(row=0, column=0, sticky="nsew")

        self.show_frame("LoginScreen")

    def show_frame(self, page_name, reset_page=False):
        if reset_page:
            self.frames[page_name].__init__(parent=self.container, controller=self)
        frame = self.frames[page_name]
        if hasattr(frame, 'prepare'):
            frame.prepare()
        frame.tkraise()

    def post(self, endpoint, json, additional_headers=None):
        headers = {'Content-type': 'application/json',
                   'Authorization': f'Bearer {self._session_token}'}
        if additional_headers is not None:
            headers.update(additional_headers)

        return requests.post(endpoint, json=json, headers=headers)

    def get(self, endpoint, additional_headers=None):
        headers = {'Content-type': 'application/json',
                   'Authorization': f'Bearer {self._session_token}'}
        if additional_headers is not None:
            headers.update(additional_headers)
        return requests.get(endpoint, headers=headers)

    def set_session_token(self, token):
        self._session_token = token


if __name__ == "__main__":
    C = Client()
    C.mainloop()
