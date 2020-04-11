## Git
### 1.Git的入门操作
* 创建版本库 **Create Repository**
~~~linux
$ mkdir fileName //创建新的仓库 -- new repository
$ cd fileName
$ pwd //显示当前仓库绝对路径

$ git init //将当前目录变为Git可以管理的仓库
Reinitialized existing Git repository in ***/.git/

$ git add file.xx //Step1.添加文件到Repository
$ git commit -m "recorder op" //Step.2告诉Git将文件提交

$ git status //掌握仓库的状态 修改状态
$ git diff Git.md //看具体修改内容
~~~

