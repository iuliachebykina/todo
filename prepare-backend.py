with open("backend_env", "r") as f:
    env = f.read().split("\n")

with open("backend/src/main/resources/application.yaml", "r") as f:
    config = f.read()

for i in [x for x in env if x]:
    splt = i.split("=")
    config = config.replace("${"+splt[0]+"}", splt[1])


with open("backend/src/main/resources/application.yaml", "w") as f: f.write(config)