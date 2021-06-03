from argparse import ArgumentParser
import subprocess
from subprocess import Popen, PIPE
import multiprocessing
import time
import sys

NUM_DISPATCHER_NODES = 1
NUM_EXECUTOR_NODES = 3

WORKING_DIRECTORY = "C:/Users/simon/Documents/GitHub/dsmt/erl"

START_TERMINAL = "start /wait"

SETTING_SHELL = True


def configuration():
    global NUM_DISPATCHER_NODES, NUM_EXECUTOR_NODES, WORKING_DIRECTORY, START_TERMINAL, SETTING_SHELL
    ap = ArgumentParser()
    ap.add_argument("-w", "--working_dir", help="Erlang working directory (where .bean files are)")
    ap.add_argument("-ns", "--noshell", action='store_false', help="Disable the executor shells (default active)")

    args=vars(ap.parse_args())

    if args['working_dir'] is not None: WORKING_DIRECTORY = args['working_dir']
    SETTING_SHELL = args['noshell']

    print("SETTED SHELL:" + str(SETTING_SHELL))

    if sys.platform.startswith('win32'):
        START_TERMINAL = "start /wait"
    elif sys.platform.startswith('linux'):
        START_TERMINAL = "gnome-terminal -x"



def generate_node_names():
    list = []
    list = []
    for disp in range(1,NUM_DISPATCHER_NODES+1):
        name = "disp"+str(disp)+"@localhost"
        list.append(name)
    for exec in range(1, NUM_EXECUTOR_NODES + 1):
        name = "exec"+str(exec)+"@localhost"
        list.append(name)
    return list


def create_node(name, connectTo):
    if connectTo == "":
        subprocess.call(START_TERMINAL + ' erl -sname ' + name + ' -s das startup '+ WORKING_DIRECTORY + ' frist', shell=True)
    else:
        print(SETTING_SHELL)
        if SETTING_SHELL == False:
            print(name)
            subprocess.call(START_TERMINAL + ' erl -sname ' + name + ' -noshell -s das startup '+ WORKING_DIRECTORY + ' '+ connectTo, shell=False)
        else:
            subprocess.call(START_TERMINAL + ' erl -sname ' + name + ' -s das startup '+ WORKING_DIRECTORY + ' '+ connectTo, shell=True)

    #subprocess.call('start /wait erl -sname '+ name + ' < input1.txt', shell=True)
    #subprocess.call('start /wait erl -sname ' + name + ' -s das startup node  '+name, shell=True)
    
    
if __name__ == "__main__":
    configuration()
    name_list = generate_node_names()
    print("CONFIGURATION SETTINGS -> W: ", WORKING_DIRECTORY, " D: ", NUM_DISPATCHER_NODES, " E: ", NUM_EXECUTOR_NODES, " SHELL:", str(SETTING_SHELL))
    print("GENERATED NODE NAMES -> ", name_list)
    try:
        fristNode = None
        for name in name_list:
            if fristNode == None:
                fristNode = name
                t = multiprocessing.Process(target=create_node, args=(name, ""))
                t.start()
                time.sleep(5)
            else:
                t = multiprocessing.Process(target=create_node, args=(name, fristNode))
                t.start()

    except:
        print("not enable to start thread")