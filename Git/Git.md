## Git
### 1.Git的入门操作
* 创建版本库 **Create Repository**
```linux
$ mkdir fileName //创建新的仓库 -- new repository
$ cd fileName
$ pwd //显示当前仓库绝对路径

$ git init //将当前目录变为Git可以管理的仓库
Reinitialized existing Git repository in ***/.git/

$ git add file.xx //Step1.添加文件到Repository
$ git commit -m "recorder op" //Step.2告诉Git将文件提交

$ git status //掌握仓库的状态 修改状态
$ git diff Git.md //看具体修改内容

$ git log (--pretty=oneline)//打印修改的历史记录

$ git reset --hard HEAD^ //回退版本 HEAD^表示上一版本
$ cat Git.md //查看内容
$ git reset --hard commit id //按id回退到之前
$ git reflog //记录命令
```
* 工作区 **Working Directory**\
![working directory](https://github.com/foooever/figure/blob/master/Java2020/%E5%9F%BA%E7%A1%80/01.jfif)\
`git add`是将文件添加进`stage`中，`git commit`将所有暂存区的内容提交到当前分支
可以用`$ git diff HEAD -- Git.md`查看工作区和版本库中最新版本的区别
```
$ git checkout -- Git.md //可以让文件返回到最近一次git commit或git add的状态
$ git reset HEAD Git.md //将暂存区的而修改撤销即未git commit的内容撤销

$ git rm Git.md //删除文件，然后进行git commit提交删除
```
* 远程仓库
```
$ github上创建新的Repository
$ git remote add origin git@github.com:foooever/Java2020.git
$ git push -u origin master //Step.3首次推送时 -u
$ git push origin master //之后推送
```



