def read_input(name):
    with open("./data/" + name, mode="r", encoding="utf-8") as file:
        return file.read().strip()
