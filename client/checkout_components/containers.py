import tkinter as tk


class StaticContainerElement(tk.Frame):
    def __init__(self, parent, text: str):
        tk.Frame.__init__(self, parent)
        tk.Label(self, text=text).pack()


class StaticContainer(tk.Frame):
    def __init__(self, parent, width, height, title, background=None):
        tk.Frame.__init__(self, parent, width=300, height=height, background=background, borderwidth=2)
        # self.pack_propagate(0)
        self.parent = parent
        tk.Label(self, text=title, font=("Verdana", 20)).pack(side=tk.TOP, anchor=tk.N)
        self.list_elements = []

    def refresh_elements(self, new_elements: [str]):
        [element.destroy() for element in self.list_elements]
        self.list_elements.clear()
        for i, element_str in enumerate(new_elements):
            self.list_elements.append(StaticContainerElement(self, element_str))
            self.list_elements[i].pack(side=tk.TOP)

    def clear(self):
        for i, entry in enumerate(self.list_elements):
            entry.destroy()
            del self.list_elements[i]
