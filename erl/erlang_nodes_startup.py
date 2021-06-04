from argparse import ArgumentParser
import subprocess
from subprocess import Popen, PIPE
import multiprocessing
import time
import sys

NUM_DISPATCHER_NODES = 1
NUM_EXECUTOR_NODES = 3

def configuration():
    working_dir = "C:/Users/simon/Documents/GitHub/dsmt/erl"
    initialization = "start"
    shell = True
    system = "start /wait"
    ap = ArgumentParser()
    ap.add_argument("-w", "--working_dir", help="Erlang working directory (where .bean files are)")
    ap.add_argument("-i", "--initialization", action='store_true', help="option that initialize mnesia database if is not present")
    ap.add_argument("-ns", "--noshell", action='store_false', help="Disable the executor shells (default active)")

    args=vars(ap.parse_args())

    if args['working_dir'] is not None: 
        working_dir = args['working_dir']
    if args['initialization'] is True:
        initialization = "init"
    if args['noshell'] is False: 
        shell = False

    if sys.platform.startswith('win32'):
        system = "start /wait"
    elif sys.platform.startswith('linux'):
        system = "gnome-terminal -x"

    return working_dir, initialization, shell, system



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
    wd, init, sh, sy = configuration()
    if connectTo == "":
        subprocess.call(sy + ' erl -sname ' + name + ' -s das startup '+ init + ' ' + wd + ' frist', shell=True)
        print(sy + ' erl -sname ' + name + ' -s das startup '+ init + ' ' + wd + ' frist')
    else:
        if sh == False:
            print(name)
            subprocess.call(sy + ' erl -sname ' + name + ' -noshell -s das startup '+ init + ' ' + wd + ' '+ connectTo, shell=True)
            print(sy + ' erl -sname ' + name + ' -noshell -s das startup '+ init + ' ' + wd + ' ')
        else:
            subprocess.call(sy + ' erl -sname ' + name + ' -s das startup '+ init + ' ' + wd + ' '+ connectTo, shell=True)
            print(sy + ' erl -sname ' + name + ' -s das startup '+ init + ' ' + wd + ' ')

    #subprocess.call('start /wait erl -sname '+ name + ' < input1.txt', shell=True)
    #subprocess.call('start /wait erl -sname ' + name + ' -s das startup node  '+name, shell=True)
    
    
if __name__ == "__main__":
    wd, init, sh, sy = configuration()
    name_list = generate_node_names()
    print("CONFIGURATION SETTINGS ->I: ", init, " W: ", wd, " D: ", NUM_DISPATCHER_NODES, " E: ", NUM_EXECUTOR_NODES, " SHELL:", str(sh))
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