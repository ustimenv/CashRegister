import tkinter as tk

from checkout_screen import CheckoutScreen
from login_screen import LoginScreen


class Client(tk.Tk):
    username = None
    session_token = None

    def __init__(self, *args, **kwargs):
        tk.Tk.__init__(self, *args, **kwargs)
        self.geometry('1080x720')
        self.title('Till')
        self.route = "http://localhost:8080"
        container = tk.Frame(self)
        container.pack(side="top", fill="both", expand=True)
        container.grid_rowconfigure(0, weight=1)
        container.grid_columnconfigure(0, weight=1)

        self.frames = {}
        for F in (LoginScreen, CheckoutScreen):
            page_name = F.__name__
            frame = F(parent=container, controller=self)
            self.frames[page_name] = frame
            # stack the frames on top of each other, the raised one will be the one visible
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
