import sys, getopt

import json

def create_ble_event(nearest_rssi, mac, start_time, nearest_time, end_time):
  # {"nearest_rssi": -61, "start_time": 1480191915.954596, "topic": "cart/cartId/ble",
  # "mac": "34:b1:f7:d3:9d:a3", "end_time": 1480191919.968618, "nearest_time": 1480191915.954596}
  event = {}
  event["topic"] = "cart/cartId/ble"
  event["nearest_rssi"] = nearest_rssi
  event["mac"] = mac
  event["start_time"] = start_time
  event["nearest_time"] = nearest_time
  event["end_time"] = end_time    
  return event

def create_revolution_event(revolution_counter, start_time):
  # {"topic": "cart/cartId/revolution", "revolution_counter": 1, "start_time": 1480191833.059162}
  event = {}
  event["topic"] = "cart/cartId/revolution"
  event["start_time"] = start_time
  event["revolution_counter"] = revolution_counter
  return event

def make_fake_trip(outfile_name):
  outfile = open(outfile_name, 'w+')
  revolution_counter = 0
  time = 0.0
  
  time = time + 100.0
  
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:91:c8', start_time = time - 15, nearest_time = time, end_time = time + 15)
  outfile.write(json.dumps(ble_event) +'\n')
  
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:9c:cb', start_time = time - 15, nearest_time = time, end_time = time + 15)
  outfile.write(json.dumps(ble_event) + '\n')  
  
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:91:e4', start_time = time - 15, nearest_time = time, end_time = time + 15)
  outfile.write(json.dumps(ble_event) + '\n') 
    
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:9d:eb', start_time = time - 15, nearest_time = time, end_time = time + 15)
  outfile.write(json.dumps(ble_event) + '\n') 
  
  for i in range(10):
    revolution_counter += 1
    time = time + 200.0
    outfile.write(json.dumps(create_revolution_event(revolution_counter, time)) +'\n')
  time = time + 100.0
  ble_event = create_ble_event(nearest_rssi = -70, mac = '34:b1:f7:d3:91:c8', start_time = time - 15, nearest_time = time, end_time = time + 15)
  outfile.write(json.dumps(ble_event) + '\n')   
  
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
