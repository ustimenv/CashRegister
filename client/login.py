import tkinter as tk
from functools import partial
from time import sleep

import requests


class LoginPage(tk.Frame):

    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        self.controller = controller

        # username/password input container
        self.login_form = tk.Frame(self, width=500, height=500)
        self.login_form.place(relx=.5, rely=.5, anchor="center")

        username_label = tk.Label(self.login_form, text="User Name").grid(row=1, column=0)
        username = tk.StringVar()
        username_input = tk.Entry(self.login_form, textvariable=username).grid(row=1, column=1)

        password_label = tk.Label(self.login_form, text="Password").grid(row=2, column=0)
        password = tk.StringVar()
        password_input = tk.Entry(self.login_form, textvariable=password, show='*').grid(row=2, column=1)

        login_function = partial(self._do_login, username, password)
        loginButton = tk.Button(self.login_form, text="Log in", command=login_function).grid(row=3, column=0)

    def _do_login(self, username, password):
        req = requests.post("http://localhost:8080", data={'username': username.get(), 'password': password.get()})
        print("username entered :", username.get())
        print("password entered :", password.get())
        if True:
            try:
                self.controller.session_token = req.json().get('session_token')
                self.controller.username = username.get()
                self._set_feedback('Okay')
                sleep(1)
                self.controller.show_frame('Till')
            except Exception as e:
                print(e)

        self._set_feedback('Invalid credentials')

    def _set_feedback(self, feedback):
        self.feedback = tk.Text(self, width=20, height=1)
        self.feedback.grid(row=0, column=0, padx=(self.winfo_width() / 2, self.winfo_width() / 2))
        self.feedback.config(state=tk.NORMAL, background=self['bg'])
        self.feedback.delete(1.0, tk.END)
        self.feedback.insert(tk.END, feedback)
