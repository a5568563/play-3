#!/usr/bin/env python
# coding: utf-8

# In[1]:


import requests
import json
from bs4 import BeautifulSoup
import pandas
import pymysql
from sqlalchemy import create_engine
import pymysql.cursors
import datetime
url = 'http://news.4399.com//wzlm/zixun/gg/m/874997.html'
res = requests.get(url)
res.encoding = 'gbk'
soup = BeautifulSoup(res.text)
print(soup.select('.le-tex'))


# In[2]:


def Check(url):
    if (url[0] == '/'):
        return True
    else:
        return False


# In[3]:


def GetRealUrl(url):
    basic_src = "http://news.4399.com/"
    result = basic_src + url
    return result


# In[4]:


def GetNewsList(url):
    res = requests.get(url)
    res.encoding = 'gbk'
    soup = BeautifulSoup(res.text)
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
    title = soup.select('.tl-tit')
    for i in range(len(title)):
        if(Check(soup.select('.tl-tit')[i].a['href'])):
            result['section_id'].append(2)
            result['title'].append(soup.select('.tl-tit')[i].text.replace('/',''))
            result['title_picture'].append(soup.select('.tl-img')[i].img['src'])
            result['content'].append(soup.select('.tl-tit')[i].text.replace('/','')+".txt")
            result["CTR"].append(0)
            result["createtime"].append(soup.select('.tl-tit')[i].span.text)
            result['url'].append(GetRealUrl(soup.select('.tl-tit')[i].a['href']))
            result['inserttime'].append(datetime.datetime.now())
            result['hot'].append(0)
        else:
            continue
    return result


# In[5]:


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


# In[6]:


result = GetNewsList('http://news.4399.com/gonglue/wzlm/zixun/44038-1.html')
df = pandas.DataFrame(result)
df = QuChong(df)
df


# In[7]:


def NewsToTxt(url, title):
    news_res = requests.get(url)
    news_res.encoding = 'gbk'
    news_soup = BeautifulSoup(news_res.text)
    content = str(news_soup.select('.le-tex')[0])
    static_src = "E:/MyEclipse_workspace/play/src/main/resources/resources/text/News/王者荣耀/"
    fo = open(static_src + title + ".txt", "w",encoding='utf-8')
    fo.write(content)
    fo.close()
    return


# In[8]:


for i in  range(6):
    if (i == 0):
        result = GetNewsList('http://news.4399.com/gonglue/wzlm/zixun/44038-1.html')
        df = QuChong(pandas.DataFrame(result))
    else:
        result = GetNewsList('http://news.4399.com/gonglue/wzlm/zixun/44038-'+str(i+1)+'.html')
        pdf = QuChong(pandas.DataFrame(result))
        df = pandas.concat([df,pdf],axis=0,ignore_index=True)
df


# In[9]:


df = df.reset_index(drop=True)
for i in range(len(df)):
    NewsToTxt(df['url'][i], df['title'][i])
del df['url']
df


# In[10]:


yconnect = create_engine('mysql+pymysql://root:123@localhost:3306/play?charset=utf8')
pandas.io.sql.to_sql(df,'news', yconnect, schema='play', if_exists='append',index=False)

