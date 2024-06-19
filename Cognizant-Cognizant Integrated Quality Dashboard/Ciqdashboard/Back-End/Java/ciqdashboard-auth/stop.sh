#!/bin/bash
kill $(cat ./pid.file) && rm nohup.out
