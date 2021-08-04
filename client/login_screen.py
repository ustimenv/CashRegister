import tkinter as tk
from functools import partial

import requests


class LoginScreen(tk.Frame):
    _login_endpoint = "/login"

    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        self.controller = controller
        self._login_endpoint = self.controller.cashier_url + self._login_endpoint

        # username/password input container
        self.login_form = tk.Frame(self, width=500, height=500)
        self.login_form.place(relx=.5, rely=.5, anchor="center")

        tk.Label(self.login_form, text="Username").grid(row=1, column=0)
        username = tk.StringVar()
        tk.Entry(self.login_form, textvariable=username).grid(row=1, column=1)

        tk.Label(self.login_form, text="Password").grid(row=2, column=0)
        password = tk.StringVar()
        tk.Entry(self.login_form, textvariable=password, show='*').grid(row=2, column=1)

        login_function = partial(self._do_login, username, password)
        tk.Button(self.login_form, text="Log in", command=login_function).grid(row=3, column=0)

    def _do_login(self, username, password):

        input_name = username.get()
        input_password = password.get()
        input_name = 'A'
        input_password = 'pass'

        if len(input_name) > 0 and len(input_password) > 0:
            try:
                headers = {'Content-type': 'application/json'}
                req = requests.post(self._login_endpoint,
                                    json={'username': input_name, 'password': input_password}, headers=headers)
                if req.status_code == 200:
                    self.controller.session_token = req.content.decode('utf-8')
                    self.controller.username = input_name
                    self._set_feedback('Okay')
                    self.controller.show_frame('CheckoutScreen')

                else:
                    self._set_feedback('Invalid credentials')

            except Exception as e:
                print(e)

        else:
            self._set_feedback('Make sure to type in both your username and password')

    def _set_feedback(self, feedback):
        self.feedback = tk.Text(self, width=20, height=1)
        self.feedback.grid(row=0, column=0, padx=(self.winfo_width() / 2, self.winfo_width() / 2))
        self.feedback.config(state=tk.NORMAL, background=self['bg'])
        self.feedback.delete(1.0, tk.END)
        self.feedback.insert(tk.END, feedback)
