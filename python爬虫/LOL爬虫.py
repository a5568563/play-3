#!/usr/bin/env python
# coding: utf-8

# In[9]:


import requests
import json
from bs4 import BeautifulSoup
import pandas
import pymysql
from sqlalchemy import create_engine
import pymysql.cursors
import datetime
url = 'http://lol.duowan.com/tag/131026281878.html'
res = requests.get(url)
res.encoding = 'utf-8'
soup = BeautifulSoup(res.text)
print(soup)


# In[10]:


soup.body.div


# In[11]:


def NewsToTxt(url, title):
    title = title.replace('/','')
    news_res = requests.get(url)
    news_res.encoding = 'utf-8'
    news_soup = BeautifulSoup(news_res.text)
    content = str(news_soup.select('#text')[0])
    static_src = "E:/MyEclipse_workspace/play/src/main/resources/resources/text/News/LOL/"
    fo = open(static_src + title + ".txt", "w",encoding='utf-8')
    fo.write(content)
    fo.close()
    return


# In[12]:


def GetRealUrl(url):
    basic_src = "http://lol.duowan.com"
    result = basic_src + url
    return result


# In[13]:


def Check(url):
    if (url[0] == '/'):
        return True
    else:
        return False


# In[14]:


def QuChong(result):
    connect = pymysql.Connect(
    host='localhost',
    port=3306,
    user='root',
    passwd='123',
    db='play',
    charset='utf8'
    )
    cursor = connect.cursor()
    sql = 'select title from news'
    cursor.execute(sql)
    rs = cursor.fetchall()
    df = pandas.DataFrame(list(rs))
    connect.close()
    for i in range(len(result)):
        for j in range(len(df)):
            if(result['title'][i] == df[0][j]):
                result.drop([i],inplace=True)
                break;
    return result


# In[15]:


def GetNewsList(url):
    res = requests.get(url)
    res.encoding = 'utf-8'
    soup = BeautifulSoup(res.text)
    title = soup.select('.titles')
    img = soup.select('.item-cover')
    createtime = soup.select('.item-time')
    result = {}
    result['section_id'] = list()
    result['title'] = list()
    result['title_picture'] = list()
    result['content'] = list()
    result['CTR'] = list()
    result['createtime'] = list()
    result['url'] = list()
    result['inserttime'] = list()
    result['hot'] = list()
    href = soup.select('.titles')
    
    for i in range(len(title)-1):
        if(Check(href[i].a['href'])):
            result['section_id'].append(1)
            result['title'].append(title[i].text.replace('/',''))
            result['title_picture'].append(img[i].img['src'])
            result['content'].append(title[i].text.replace('/','')+".txt")
            result['CTR'].append(0)
            result['createtime'].append(createtime[i].text)
            result['url'].append(GetRealUrl(href[i].a['href']))
            result['inserttime'].append(datetime.datetime.now())
            result['hot'].append(0)
        else:
            continue
    
    return result
        


# In[16]:


df = pandas.DataFrame()
pdf = pandas.DataFrame()
for i in  range(6):
    if (i == 0):
        result = GetNewsList('http://lol.duowan.com/tag/131026281878.html')
        df = QuChong(pandas.DataFrame(result))
    else:
        result = GetNewsList('http://lol.duowan.com/tag/131026281878_'+str(i+1)+'.html')
        pdf = QuChong(pandas.DataFrame(result))
        df = pandas.concat([df,pdf],axis=0,ignore_index=True)
df


# In[17]:


df = df.reset_index(drop=True)
for i in range(len(df)):
    NewsToTxt(df['url'][i], df['title'][i])
del df['url']
df


# In[18]:


yconnect = create_engine('mysql+pymysql://root:123@localhost:3306/play?charset=utf8')
pandas.io.sql.to_sql(df,'news', yconnect, schema='play', if_exists='append',index=False)

