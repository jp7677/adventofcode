def read_input(name):
    input_data = open("./data/" + name, "r")
    data = input_data.read().strip()
    input_data.close()
    return data
