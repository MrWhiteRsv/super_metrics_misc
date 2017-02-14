import requests
import sys, getopt

import json

def get_ndbo(uuid):
  headers = {'Content-Type':'application/json'}
  url = 'http://api.nal.usda.gov/ndb/search?'
  payload = {'q': uuid, 'max': '2500', 'offset' : '0', 'api_key':'DEMO_KEY'}
  res = requests.post(url, headers=headers,  params=payload).json()
  ndbno = res['list']['item'][0]['ndbno']
  return ndbno
   
def get_report(ndbo):
  headers = {'Content-Type':'application/json'}
  url = 'http://api.nal.usda.gov/ndb/reports'
  payload = {"ndbno":ndbo,"type":"b", 'api_key':'DEMO_KEY'}
  return requests.post(url, headers=headers,  params=payload).json()

def extract_nutrients_from_report(report):
  nutrients_lst = report['report']['food']['nutrients']
  result = []
  for nutrient in nutrients_lst:
    slim_nutrient = {}
    slim_nutrient['name'] = nutrient['name']
    slim_nutrient['value'] = nutrient['value']
    slim_nutrient['unit'] = nutrient['unit']
    result.append(slim_nutrient)
    #print ('slim_nutrient', slim_nutrient)
  return result

def make_queries(infile_name, outfile_name):
  infile = open(infile_name)
  outfile = open(outfile_name, 'w+')
  
  for line in infile:
    if line.startswith('#'):
      continue
    entry = {}
    if (len(line) == 1):
      continue
    try:
      parsed_line = json.loads(line)
    except ValueError:
      print("Could not parse line: " + line)
      continue
    
    uuid = parsed_line['uuid']
    entry['uuid'] = uuid
    if 'ndbno' in parsed_line:
      ndbno = parsed_line['ndbno']
    else:
      ndbno = get_ndbo(uuid)
    report = get_report(ndbno)
    # print (report)
    entry['name'] = parsed_line['name']
    entry['all_photo_urls'] = parsed_line['all_photo_urls']
    entry['price'] = parsed_line['price']
    entry['location_px'] = parsed_line['location_px']
    entry['location_str'] = parsed_line['location_str']
    entry['discount_percent'] = parsed_line['discount_percent']
    entry['category'] = parsed_line['category']
    entry['ndbno'] = ndbno
    food = report['report']['food']
    entry['description'] = food['name']
    if 'ing' in food:
      entry['ingridiants'] = food['ing']['desc']
    entry['nutrients'] = extract_nutrients_from_report(report)
    outfile.write(json.dumps(entry) + '\n\n')
  infile.close()
  outfile.close()
  


def main(argv):
  try:
    opts, args = getopt.getopt(argv, "hi:o:", ["infile=", 'outfile='])
  except getopt.GetoptError:
    print 'usda_query.py -i <query_file> -o <res_file>'
    sys.exit(2)
  for opt, arg in opts:
    if opt == '-h':
      print 'python usda_query.py -i <log_file> -o <res_file>'
      sys.exit()
    if opt in ("-i", "--infile"):
      infile = arg
    if opt in ("-o", "--outfile"):
      outfile = arg
  make_queries(infile, outfile)
  
if __name__ == '__main__':
    main(sys.argv[1:])
