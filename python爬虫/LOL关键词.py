#!/usr/bin/env python
# coding: utf-8

# In[12]:


import pandas
import pymysql
from sqlalchemy import create_engine
import pymysql.cursors
import jieba as jb
import jieba.analyse
import operator
from decimal import Decimal
jb.load_userdict('E:/MyEclipse_workspace/play/src/main/resources/resources/text/userdict.txt')


# In[13]:


def stopwordslist():
    stopwords = [line.strip() for line in open('E:/MyEclipse_workspace/play/src/main/resources/resources/text/stopword.txt',encoding='UTF-8').readlines()]
    return stopwords


# In[14]:


def seg_depart(sentence):
    stopwords = stopwordslist()
    outstr = ''
    sentence = sentence.replace('\n', '' )
    sentence = sentence.replace('\u3000', '' )
    sentence = sentence.replace('，', '' )
    sentence = sentence.replace('。', '' )
    sentence_depart = jb.cut(sentence.strip())
    for word in sentence_depart:
         if word not in stopwords:
            if (word != '\t') & (word !=" "):
                if len(word) >= 2:
                    outstr += word
                    outstr += " "
    return outstr


# In[15]:


def FileOperation(src):
    static_src = "E:/MyEclipse_workspace/play/src/main/resources/resources/text/News/LOL/"
    src = static_src + src
    inputs = open( src , "r",encoding='utf-8')
    outputs = str()
    for line in inputs:
        line_seg = seg_depart(line)
        outputs+=line_seg
    inputs.close()
    seg = jb.cut(outputs)
    word_dict = {}
    num = 0
    for item in seg:
        num += 1
        if item not in word_dict:
            word_dict[item] = 1
        else:
            word_dict[item] += 1
    for item in word_dict:
        flag = Decimal(word_dict[item] / num).quantize(Decimal('0.000'))
        if flag >= 0.3 :
            word_dict[item] = 0
        word_dict[item] = str(word_dict[item])
    return word_dict,num


# In[16]:


def sort_dict(dict_words):
    """
    字典排序
    :param dict_words:
    :return:
    """
    keys = dict_words.keys()
    values = dict_words.values()
 
    list_one = [(key, val) for key, val in zip(keys, values)]
    list_sort = sorted(list_one, key=lambda x: x[1], reverse=True)
    return list_sort


# In[17]:


def Get_3day_new():
    connect = pymysql.Connect(
        host='localhost',
        port=3306,
        user='root',
        passwd='123',
        db='play',
        charset='utf8'
        )
    cursor = connect.cursor()
    sql = 'select news_id,content from news where section_id = 1 '
    cursor.execute(sql)
    rs = cursor.fetchall()
    df = pandas.DataFrame(list(rs),columns=['news_id','content'])
    connect.close()
    return df


# In[18]:


def clear_keylist():
    connect = pymysql.Connect(
        host='localhost',
        port=3306,
        user='root',
        passwd='123',
        db='play',
        charset='utf8'
        )
    try:
        cursor = connect.cursor()
        sql = 'delete from keylist where 1 = 1'
        cursor.execute(sql)
        cursor.close()
        connect.commit()
    except Exception as err:
        connect.rollback()
        raise err
    finally:
        connect.close()


# In[19]:


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
    sql = 'select new_id from keylist'
    cursor.execute(sql)
    rs = cursor.fetchall()
    df = pandas.DataFrame(list(rs))
    connect.close()
    for i in range(len(result)):
        for j in range(len(df)):
            if(result['new_id'][i] == df[0][j]):
                result.drop([i],inplace=True)
                break;
    return result


# In[1]:


def main():
    
    df = Get_3day_new()
    word_dict = {}
    key_list = list()
    num_list = list()
    new_id_list = list()
    section_id_list = list()
    for i in range(len(df)):
        keylist = str()
        temp_dict , num = FileOperation(df['content'][i])
        new_id_list.append(df['news_id'][i])
        section_id_list.append(1)
        num_list.append(num)
        temp_dict = sort_dict(temp_dict)
        for i in range(min(10,len(temp_dict))):
            keylist += (":".join(temp_dict[i]))+(";")
        key_list.append(keylist)
    c = {
        "new_id":new_id_list,
        "section_id":section_id_list,
        "total_num": num_list,
        "key_group": key_list
    }
    df = pandas.DataFrame(c)
    df = QuChong(df)
    print(df)
    write_sql(df)


# In[21]:


def write_sql(df):
    yconnect = create_engine('mysql+pymysql://root:123@localhost:3306/play?charset=utf8')
    pandas.io.sql.to_sql(df,'keylist', yconnect, schema='play', if_exists='append',index=False)


# In[24]:


main()


# In[ ]:




