import requests
import sys, getopt

import json

def crete_ble_event(rssi, mac, time_sec):
  # {"topic": "cart/cartId/raw_ble", "rssi": -84, "mac": "34:b1:f7:d3:9d:a3", "time_sec": 1480191901.882946}
  event = {}
  event["topic"] = "cart/cartId/raw_ble"
  event["rssi"] = rssi
  event["mac"] = mac
  event["time_sec"] = time_sec
  return event

def create_revolution_event(revolution_counter, start_time):
  # {"topic": "cart/cartId/revolution", "revolution_counter": 1, "start_time": 1480191833.059162}
  event = {}
  event["topic"] = "cart/cartId/revolution"
  event["start_time"] = start_time
  event["revolution_counter"] = revolution_counter
  return event


def make_fake_trip(outfile_name):
  """
        this.beaconsGraph.addEdgeLength('34:b1:f7:d3:91:c8', '34:b1:f7:d3:9c:cb', 20);
      this.beaconsGraph.addEdgeLength('34:b1:f7:d3:9c:cb', '34:b1:f7:d3:91:e4', 200);
      this.beaconsGraph.addEdgeLength('34:b1:f7:d3:91:e4', '34:b1:f7:d3:9d:eb', 20);
      this.beaconsGraph.addEdgeLength('34:b1:f7:d3:9d:eb', '34:b1:f7:d3:91:c8', 200);
  """
  outfile = open(outfile_name, 'w+')
  revolution_counter = 0
  time = 0.0
  
  time = time + 100.0
  outfile.write(json.dumps(crete_ble_event(rssi = -70, mac = '34:b1:f7:d3:91:c8', time_sec = time)) +'\n')
  
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  outfile.write(json.dumps(crete_ble_event(rssi = -70, mac = '34:b1:f7:d3:9c:cb', time_sec = time)) +'\n')
  
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  outfile.write(json.dumps(crete_ble_event(rssi = -70, mac = '34:b1:f7:d3:91:e4', time_sec = time)) +'\n')
  
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  outfile.write(json.dumps(crete_ble_event(rssi = -70, mac = '34:b1:f7:d3:9d:eb', time_sec = time)) +'\n')
  
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  outfile.write(json.dumps(crete_ble_event(rssi = -70, mac = '34:b1:f7:d3:91:c8', time_sec = time)) +'\n')
  
  
  outfile.close()
  
def main(argv):
  try:
    opts, args = getopt.getopt(argv, "ho:", ['outfile='])
  except getopt.GetoptError:
    print 'fake_trip_generator.py -o <res_file>'
    sys.exit(2)
  for opt, arg in opts:
    if opt == '-h':
      print 'python fake_trip_generator.py -o <res_file>'
      sys.exit()
    if opt in ("-o", "--outfile"):
      outfile = arg
  make_fake_trip(outfile)
  
if __name__ == '__main__':
    main(sys.argv[1:])
